package uk.nhs.cdss.transform.ucdos;

import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import uk.nhs.cdss.model.CheckServicesInputBundle;
import uk.nhs.cdss.model.ucdos.ClinicalTermSearch;
import uk.nhs.cdss.model.ucdos.SymptomDiscriminator;
import uk.nhs.cdss.model.ucdos.SymptomGroup;

@Component
@AllArgsConstructor
public class ClinicalTermSearchTransformer {

  private AgeGroupTransformer ageGroupTransformer;
  private GenderTransformer genderTransformer;

  public ClinicalTermSearch transform(CheckServicesInputBundle inputBundle) {
    return ClinicalTermSearch.builder()
        .caseId(inputBundle.getRequestId())
        .searchDistance(inputBundle.getSearchDistance())
        .postcode(inputBundle.getPatient().getAddressFirstRep().getPostalCode())
//        .gpPracticeId() TODO: What goes here?
        .age(ageGroupTransformer.transform(inputBundle.getPatient().getBirthDate()))
        .gender(genderTransformer.transform(inputBundle.getPatient().getGender()))
        .symptomGroupDiscriminatorCombos(ImmutableMap.of(
            SymptomGroup.ACCIDENT_OR_EMERGENCY, SymptomDiscriminator.PCR_WITH_FEATURES_OF_SUBSTANCE_OR_ALCOHOL_MISUSE
        ))
        .numberPerType(100) //??
        .build();

  }
}
