package com.solibri.rule;

/*

 Building Code 2018 of Kansas
 
 1207.2 Minimum Ceiling Heights

Occupiable spaces, habitable spaces and corridors shall have a ceiling height of not less than 7 feet 6 inches (2286 mm) 
above the finished floor. Bathrooms, toilet rooms, kitchens, storage rooms and laundry rooms 
shall have a ceiling height of not less than 7 feet (2134mm) above the finished floor.

Auther : Amarjeet Singh
Course : Software Lab 

*/

/*
This code is functional conditionally. 
This code is yet not functional for the exceptions of the subject clause.
I plan to implement Exceptions in the next semester.
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

/**
 * This rule shows how to check the Ceiling Height of Occupiable spaces,
 * habitable spaces and corridors,Bathrooms, toilet rooms, kitchens, storage
 * rooms and laundry rooms.
 * The height must not exceed 7'6" for Occupiable spaces, habitable spaces and
 * corridors.
 * The height must not exceed 7' for Bathrooms, toilet rooms, kitchens, storage
 * rooms and laundry rooms.
 * the limit is set by the user. The OneByOneRule class provides functionality
 * for checking the components that pass the default filter one by one.
 */

public final class MinimumCeilingHeight extends OneByOneRule {

    /**
     * Retrieve the rule parameter creator for this rule.
     */

    private final RuleParameters params = RuleParameters.of(this);

    // private final StringParameter stringParameter =
    // params.createString("MyStringParameter");

    /**
     * A StringParameter allows the user to input text.
     */

    final StringParameter resultNameStringParameter = params.createString("rpResultName");

    /**
     * A DoubleParameter allows the user to input a double value. The
     * PropertyType is used to correctly format the double value using the units
     * that are selected in the
     * application settings. For example, when metres are used as the unit of
     * length, then the value "5.0" will
     * be formatted as "5.0 m" in the UI.
     */

    final DoubleParameter maximumWidthDoubleParameter = params.createDouble("rpMaxWidth", PropertyType.LENGTH);

    /**
     * This method is called for every component that passes through the default
     * filter.
     *
     * @param component     the component that is checked by this method
     * @param resultFactory the factory that is used for creating results for the
     *                      checked component
     * @return a collection of results associated with the component that is checked
     *         in this method
     */

    @Override
    public Collection<Result> check(Component component, ResultFactory resultFactory) {

        // Names of the component (DE/EN: Space)

        // String componentName = resultNameStringParameter.getValue();

        if (component.getName().contains("Space")) {
            /*
             * This method returns the axis-aligned bounding box of the
             * component.
             */
            AABB3d componentBounds = component.getBoundingBox();
            // double corridorLength = componentBounds.getSizeX();

            // component.getComponentName()

            /*
             * The AABB object contains the bounds of the component in each dimension.
             * The height of a component can be retrieved by getting the size of the
             * bounding box
             * along the Z-axis.
             */

            double ceilingheight = componentBounds.getSizeZ();

            /*
             * The value that is set into a rule parameter can be accessed using the
             * getValue method.
             * This retrieves the maximum allowed height for components from the rule
             * parameter.
             */

            Double maximumAllowedheight = maximumWidthDoubleParameter.getValue();

            /*
             * Here If condition is writtten to filter out the exceptional spaces of the
             * Building
             */

            if (component.getName().contains("Bathrooms") || component.getName().contains("Restroom")
                    || component.getName().contains("Kitchen") || component.getName().contains("Storage")
                    || component.getName().contains("Laundry room")) {

                maximumAllowedheight -= 0.1524;

            }

            /*
             * Check if the ceiling height is more than criterian in the clause 1207.2,
             * Buildign code of Kansas 2018.
             */

            if (ceilingheight >= maximumAllowedheight) {

                /*
                 * Return an empty collection of results because the component height is either
                 * equal to or more than set limit
                 */

                return Collections.emptyList();
            }
            /*
             * The component's/Space height does not exceeds the allowed minimum, so a
             * result is created for the
             * component.
             */

            /*
             * The string parameter is used for getting the name of the result.
             */

            String resultName = resultNameStringParameter.getValue();

            /*
             * Create a formatted string of the ceiling height of space. The format uses the
             * units that are set in the application settings.
             */

            String formattedComponentHeight = PropertyType.LENGTH.getFormat().format(ceilingheight);

            /*
             * Create a description of the result using the formatted ceilignheight.
             */

            String resultDescription = MessageFormat
                    .format("The Width of component {0} exceeds the allowed maximum width {1} & {2}",
                            component.getName(), formattedComponentHeight, ceilingheight);

            /*
             * Create a result with the name and description. In the check method of
             * OneByOneRule
             * the returned results are automatically associated with the component that is
             * passed
             * as a parameter.
             */

            Result result = resultFactory.create(resultName, resultDescription);

            /*
             * Only a single result is created for the component, so a singleton Set is
             * created for the results.
             */

            return Collections.singleton(result);

        }

        else {
            return Collections.emptyList();
        }

    }

}
