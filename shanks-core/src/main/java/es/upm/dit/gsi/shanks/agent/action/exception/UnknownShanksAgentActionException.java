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
/**
 * es.upm.dit.gsi.shanks
 * 02/04/2012
 */
package es.upm.dit.gsi.shanks.agent.action.exception;

import es.upm.dit.gsi.shanks.exception.ShanksException;

/**
 * Signals that a non-defined action identified with actionID has been called from
 * a specific agent identified with an agentID. 
 * 
 * @author -
 *
 */
public class UnknownShanksAgentActionException extends ShanksException {

    /**
     * 
     * @param actionID 
     *              The undefined action required. 
     * @param agentID 
     *              Agent which was called to perform the undefined action.   
     */
    public UnknownShanksAgentActionException(String actionID, String agentID) {
        super("Unknown action " + actionID + "for agent " + agentID);
    }

    private static final long serialVersionUID = -1884223740174508179L;
}
