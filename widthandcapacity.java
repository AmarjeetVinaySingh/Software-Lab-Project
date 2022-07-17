package com.solibri.smc.api.examples.beginner;

/* This code as of now just filters out all the corridors as per the filter set by the user.Futher development planned in next semester */

/*1020.2 Width and Capacity

The required capacity of corridors shall be determined as specified in Section 1005.1, but the minimum width shall be not less than that specified in Table 1020.2.

Exception: In Group I-2 occupancies, corridors are not required to have a clear width of 96 inches (2438 mm) in areas where there will not be stretcher or bed movement for access to care or as part of the defend-in-place strategy.

TABLE 1020.2
MINIMUM CORRIDOR WIDTH


OCCUPANCY	                                                                           MINIMUM WIDTH
                                                                                        (inches)
Any facility not listed in this table	                                                  44
Access to and utilization of mechanical, plumbing or electrical systems or equipment	  24
With an occupant load of less than 50	                                                  36
Within a dwelling unit	                                                                36
In Group E with a corridor having an occupant load of 100 or more	                      72
In corridors and areas serving stretcher traffic in ambulatory care facilities	        72
Group I-2 in areas where required for bed movement	                                    96
For SI: 1 inch = 25.4 mm.


*/

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;

import com.solibri.geometry.primitive3d.AABB3d;
import com.solibri.smc.api.checking.DoubleParameter;
import com.solibri.smc.api.checking.OneByOneRule;
import com.solibri.smc.api.checking.Result;
import com.solibri.smc.api.checking.ResultFactory;
import com.solibri.smc.api.checking.RuleParameters;
import com.solibri.smc.api.checking.StringParameter;
import com.solibri.smc.api.model.Component;
import com.solibri.smc.api.model.PropertyType;

public final class widthandcapacity extends OneByOneRule {

    // minimum corridor width
    // this code is created for Software lab

    private final RuleParameters params = RuleParameters.of(this);

    // private final StringParameter stringParameter =
    // params.createString("MyStringParameter");

    final StringParameter resultNameStringParameter = params.createString("rpResultName");

    final DoubleParameter maximumWidthDoubleParameter = params.createDouble("rpMaxWidth", PropertyType.LENGTH);

    @Override
    public Collection<Result> check(Component component, ResultFactory resultFactory) {

        // Names of the component (DE/EN: Corridor)
        // String componentName = resultNameStringParameter.getValue();

        if (component.getName().contains("Corridor")) {

            AABB3d componentBounds = component.getBoundingBox();
            // double corridorLength = componentBounds.getSizeX();
            double corridorWidth = componentBounds.getSizeZ();
            // double corridorwidth = componentBounds.getSizeX();

            Double maximumAllowedwidth = maximumWidthDoubleParameter.getValue();

            if (corridorWidth >= maximumAllowedwidth) {

                return Collections.emptyList();
            }

            String resultName = resultNameStringParameter.getValue();

            String formattedComponentHeight = PropertyType.LENGTH.getFormat().format(corridorWidth);

            String resultDescription = MessageFormat
                    .format("The Width of component {0} exceeds the allowed maximum width {1}",
                            component.getName(), formattedComponentHeight);

            Result result = resultFactory.create(resultName, resultDescription);

            return Collections.singleton(result);

        }

        else {
            return Collections.emptyList();
        }

    }

}
