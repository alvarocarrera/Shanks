/*******************************************************************************
 * Copyright  (C) 2014 Álvaro Carrera Barroso
 * Grupo de Sistemas Inteligentes - Universidad Politecnica de Madrid
 *  
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *  
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *  
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package es.upm.dit.gsi.shanks.notification.util.test;

import java.util.logging.Logger;

import sim.engine.Steppable;
import es.upm.dit.gsi.shanks.model.element.exception.UnsupportedNetworkElementFieldException;
import es.upm.dit.gsi.shanks.model.event.networkelement.ProbabilisticNetworkElementEvent;

public class TestProbabilisticNetworkElementEvent extends ProbabilisticNetworkElementEvent{

    public TestProbabilisticNetworkElementEvent(Steppable generator) {
        super(TestDefinitions.EVENT_ID+TestProbabilisticNetworkElementEvent.class, 
                generator, TestDefinitions.EVENT_PROBABILITY, Logger.getLogger(TestPeriodicNetworkElementEvent.class.getName()));
        // TODO Auto-generated constructor stub
    }

    @Override
    public void addPossibleAffected() {
        this.addPossibleAffectedElementState(TestDevice.class, null, false);
        
    }

    @Override
    public void interactWithNE() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void changeOtherFields()
            throws UnsupportedNetworkElementFieldException {
        // TODO Auto-generated method stub
        
    }

}
