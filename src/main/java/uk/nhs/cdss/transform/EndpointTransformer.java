package uk.nhs.cdss.transform;

import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections4.Transformer;
import org.hl7.fhir.dstu3.model.Resource;
import org.springframework.stereotype.Component;
import uk.nhs.cdss.model.Endpoint;

@Component
public class EndpointTransformer implements Transformer<List<Endpoint>, List<Resource>> {

  @Override
  public List<Resource> transform(List<Endpoint> from) {
    return from.stream()
        .map(endpoint -> {
            org.hl7.fhir.dstu3.model.Endpoint ep = new org.hl7.fhir.dstu3.model.Endpoint();

            ep.setId("#" + endpoint.getId());
            ep.setAddress(endpoint.getAddress());

            return ep;
        })
        .collect(Collectors.toList());
  }
}
