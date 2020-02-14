package uk.nhs.cdss.transform.ucdos;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import uk.nhs.cdss.model.CheckServicesInputBundle;
import uk.nhs.cdss.model.ucdos.ServiceTypeSearch;

@Component
@AllArgsConstructor
public class ServiceTypeSearchTransformer {

  private AgeGroupTransformer ageGroupTransformer;
  private GenderTransformer genderTransformer;

  public ServiceTypeSearch transform(CheckServicesInputBundle inputBundle) {
    return ServiceTypeSearch.builder()
        .caseId(inputBundle.getRequestId())
        .searchDistance(inputBundle.getSearchDistance())
        .postcode(inputBundle.getPatient().getAddressFirstRep().getPostalCode())
//        .gpPracticeId() TODO: What goes here?
        .age(ageGroupTransformer.transform(inputBundle.getPatient().getBirthDate()))
        .gender(genderTransformer.transform(inputBundle.getPatient().getGender()))
        .serviceTypes(new int[]{15}) //TODO: Should we have logic to decide this?
        .numberPerType(100) //??
        .build();

  }

}
