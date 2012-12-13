package es.upm.dit.gsi.shanks.magneto.model.event.failures;

import sim.engine.Steppable;
import es.upm.dit.gsi.shanks.magneto.model.element.device.ServerGateway;
import es.upm.dit.gsi.shanks.model.element.exception.UnsupportedNetworkElementFieldException;
import es.upm.dit.gsi.shanks.model.event.failiure.Failure;

public class InternalProviderConnectivityFailure extends Failure{

	public InternalProviderConnectivityFailure(Steppable generator) {
		super(InternalProviderConnectivityFailure.class.getName(), generator, 1);
	}

	@Override
	public void changeOtherFields()
			throws UnsupportedNetworkElementFieldException {
		
	}

	@Override
	public void addPossibleAffected() {
		this.addPossibleAffectedElementProperty(ServerGateway.class, ServerGateway.PROPERTY_PROVIDERHAN, 1);
		this.addPossibleAffectedElementProperty(ServerGateway.class, ServerGateway.PROPERTY_LOCALACCESS, 4);
	}

	@Override
	public void interactWithNE() {
		
	}

}