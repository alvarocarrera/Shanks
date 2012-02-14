package es.upm.dit.gsi.shanks.model.ftth.failure;

import es.upm.dit.gsi.shanks.model.failure.Failure;
import es.upm.dit.gsi.shanks.model.ftth.element.device.DeviceDefinitions;
import es.upm.dit.gsi.shanks.model.ftth.element.device.OLT;
import es.upm.dit.gsi.shanks.model.ftth.element.device.ONT;

public class OLTEmitedLaserFailure extends Failure{

	public OLTEmitedLaserFailure() {
		super(OLTEmitedLaserFailure.class.getName(), 0.25);
	}

	
	/* (non-Javadoc)
     * @see es.upm.dit.gsi.shanks.model.failure.Failure#addPossibleAffectedElements()
     */
	@Override
	public void addPossibleAffectedElements() {
		this.addPossibleAffectedElements(OLT.class, DeviceDefinitions.NOK_STATUS);
		this.addPossibleAffectedElements(ONT.class, DeviceDefinitions.NOK_STATUS);
		
	}
	

}
