package uk.nhs.cdss.transform;

import org.apache.commons.collections4.Transformer;
import org.springframework.stereotype.Component;
import uk.nhs.cdss.model.Endpoint;

@Component
public class EndpointTransformer implements Transformer<Endpoint, org.hl7.fhir.dstu3.model.Endpoint> {

  @Override
  public org.hl7.fhir.dstu3.model.Endpoint transform(Endpoint from) {
    org.hl7.fhir.dstu3.model.Endpoint endpoint = new org.hl7.fhir.dstu3.model.Endpoint();

    endpoint.setId("#" + from.getId());
    endpoint.setAddress(from.getAddress());

    return endpoint;
  }
}
