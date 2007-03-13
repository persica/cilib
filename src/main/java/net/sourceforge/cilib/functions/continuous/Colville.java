/*
 * Colville.java
 *
 * Created on June 4, 2003, 5:00 PM
 *
 * 
 * Copyright (C) 2003 - 2006 
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science 
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA 
 *   
 */

package net.sourceforge.cilib.functions.continuous;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.Vector;

/**
 *
 * @author  engel
 */
public class Colville extends ContinuousFunction {
    
    /** Creates a new instance of Colville */
    public Colville() {
        //constraint.add(new DimensionValidator(4));
        setDomain("R(-10, 10)^4");
    }
    
    public Object getMinimum() {
        return new Double(0);
    }
    
    public double evaluate(Vector x) {
    	double a = x.getReal(0);
    	double b = x.getReal(1);
    	double c = x.getReal(2);
    	double d = x.getReal(3);
    	
        return 100 * (b-a*a) * (b-a*a)
               + (1-a) * (1-a)
               + 90 * (d-c*c) * (d-c*c)
               + (1-c) * (1-c)
               + 10.1 * ((b-1) * (b-1) + (d-1) * (d-1))
               + 19.8 * (b-1) * (d-1);
    }
    
}