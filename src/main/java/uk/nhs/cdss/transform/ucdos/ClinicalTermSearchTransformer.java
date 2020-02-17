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
  private OdsCodeTransformer odsCodeTransformer;

  public ClinicalTermSearch transform(CheckServicesInputBundle inputBundle) {
    var builder = ClinicalTermSearch.builder()
        .caseId("TEST")
        .searchDistance(inputBundle.getSearchDistance())
        .postcode(inputBundle.getLocation().getAddress().getPostalCode())
        .age(ageGroupTransformer.transform(inputBundle.getPatient().getBirthDate()).intValue())
        .gender(genderTransformer.transform(inputBundle.getPatient().getGender()))
        .symptomGroupDiscriminatorCombos(ImmutableMap.of(
            SymptomGroup.ACCIDENT_OR_EMERGENCY, SymptomDiscriminator.PCR_WITH_FEATURES_OF_SUBSTANCE_OR_ALCOHOL_MISUSE
        ))
        .numberPerType(100); //??

    if (inputBundle.getRegisteredGp() != null) {
      // SYMBOL: represents calling the UCDOS byODSCode REST API and using the result
      builder = builder.gpPracticeId(
          odsCodeTransformer.transform(inputBundle.getRegisteredGp()).hashCode());
    }

    return builder.build();

  }
}
