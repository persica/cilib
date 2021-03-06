/**
 * Computational Intelligence Library (CIlib) Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP) Department of Computer
 * Science University of Pretoria South Africa
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.type.parser;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import org.parboiled.Action;
import org.parboiled.BaseParser;
import org.parboiled.Context;
import org.parboiled.Rule;

public final class DomainParserGrammar {

    private static final BoundsFactory bf = new BoundsFactory();
    private static final Map<String, TypeCreator> creators =
            ImmutableMap.<String, TypeCreator>builder()
            .put("R", new R())
            .put("Z", new Z())
            .put("B", new B())
            .put("T", new T())
            .build();

    public static class ExpandingParser extends BaseParser<String> {

        Rule Expansion() {
            return Sequence(DomainString(), Optional(",", Expansion()), EOI);
        }

        Rule DomainString() {
            return Sequence(OneOrMore(AnyOf("RBZT():-0123456789.")), push(match()), Optional("^", Number(), push(matchOrDefault("1")), new Action<String>() {

                @Override
                public boolean run(Context<java.lang.String> context) {
                    final int number = Integer.parseInt(pop());
                    final String domain = pop();
                    for (int i = 0; i < number; i++) {
                        push(domain);
                    }
                    return true;
                }
            }));
        }

        Rule Number() {
            return Sequence(NonZeroDigit(), ZeroOrMore(Digit()));
        }

        Rule NonZeroDigit() {
            return CharRange('1', '9');
        }

        Rule Digit() {
            return CharRange('0', '9');
        }
    }

    public static class DomainGrammar extends BaseParser<Object> {

        Rule Domain() {
            return FirstOf(Text(), Numeric()); // Branch: Text or numeric, but not both
        }

        Rule Text() {
            return Sequence("T", push(creators.get("T").create()), FirstOf(Optional(",", Text()), EOI));
        }

        Rule Numeric() {
            return Sequence(Type(), push(creators.get(match())), Optional(Bounds()), new Action<TypeCreator>() {

                @Override
                public boolean run(Context<TypeCreator> context) {
                    Object o = peek();
                    if (o instanceof String) { // we have bounds ie: Tuple3[String, String, String]
                        swap3();
                        TypeCreator creator = (TypeCreator) pop();
                        String left = (String) pop();
                        String right = (String) pop();
                        if (left.equals("INF") || right.equals("INF")) {
                            push(creator.create());
                        } else {
                            push(creator.create(bf.create(Double.parseDouble(left), Double.parseDouble(right))));
                        }
                    } else { // we have a TypeCreator
                        push(((TypeCreator) pop()).create());
                    }

                    return true;
                }
            }, FirstOf(Optional(Sequence(",", Numeric())), EOI));
        }

        Rule Bounds() {
            return Sequence('(', Decimal(), push(matchOrDefault("INF")),
                    ":",
                    Decimal(), push(matchOrDefault("INF")), ')');
        }

        Rule Type() {
            return AnyOf("RZB");
        }

        Rule Digit() {
            return CharRange('0', '9');
        }

        Rule Decimal() {
            return Sequence(Optional(Ch('-')), OneOrMore(Digit()), Optional(Sequence('.', OneOrMore(Digit()))));
        }
    }
}
