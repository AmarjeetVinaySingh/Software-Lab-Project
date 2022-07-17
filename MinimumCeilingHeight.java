package com.solibri.rule;

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
Within a dwelling unit	                                                                  36
In Group E with a corridor having an occupant load of 100 or more	                      72
In corridors and areas serving stretcher traffic in ambulatory care facilities	          72
Group I-2 in areas where required for bed movement	                                      96
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

public final class MinimumCeilingHeight extends OneByOneRule {

	//minimum corridor width
	//this code is created for Software lab 
	
	private final RuleParameters params = RuleParameters.of(this);

	private final StringParameter stringParameter = params.createString("MyStringParameter");
	
	final StringParameter resultNameStringParameter = params.createString("rpResultName");
	
	final DoubleParameter maximumWidthDoubleParameter = params.createDouble("rpMaxWidth", PropertyType.LENGTH);
	
	@Override
	public Collection<Result> check(Component component, ResultFactory resultFactory) 
	{
		
		
		//Names of the component (DE/EN: Corridor)
		String componentName = resultNameStringParameter.getValue();
		
		
		
		
		if (component.getName().contains("Space"))
		{
			//main	
			AABB3d componentBounds = component.getBoundingBox();
			//double corridorLength = componentBounds.getSizeX();
			double ceilingheight = componentBounds.getSizeZ();
			//bounding box doesn't go all the way to the walls -> get the x width differently...
			//double corridorwidth = componentBounds.getSizeX();
			//
			//then print the width of the corridor
			
			Double maximumAllowedheight = maximumWidthDoubleParameter.getValue();
			
			if (component.getName().contains("Bathrooms") || component.getName().contains("Restroom") || component.getName().contains("Kitchen") || component.getName().contains("STORAGE") || component.getName().contains("laundry room") ) {
				
				
				maximumAllowedheight-=0.1524;
				
		
			}

			/*
			 * Check if the component does not exceed the maximum height.
			 */
			if (ceilingheight >= maximumAllowedheight) {
				/*
				 * Return an empty collection of results because the component height does not
				 * exceed the maximum allowed height.
				 */
				return Collections.emptyList();
			}
			
			/*
			 * The component's height exceeds the allowed minimum, so a result is created for the
			 * component.
			 */

			/*
			 * The string parameter is used for getting the name of the result.
			 */
			String resultName = resultNameStringParameter.getValue();

			/*
			 * Create a formatted string of the component height. The format uses the
			 * units that are set in the application settings.
			 */
			String formattedComponentHeight = PropertyType.LENGTH.getFormat().format(ceilingheight);

			/*
			 * Create a description of the result using the formatted component height.
			 */
			String resultDescription = MessageFormat
				.format("The Width of component {0} exceeds the allowed maximum width {1} & {2}",
					component.getName(), formattedComponentHeight, ceilingheight);

			/*
			 * Create a result with the name and description. In the check method of OneByOneRule
			 * the returned results are automatically associated with the component that is passed
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
		
		
		
		
		/*else if (resultName.contains("kitchen") || resultName.contains("Küche"))
		{
			//main
			//placeholder-return
			return null;
			
		}
		return null;
		*/
		
		//give check results
		
		
		
	}

	
	
	
	//make list only with habitable spaces
	
	//check for kitchen
	
	//check for habitable non-kitchen
	
	
	
	
	
	
	
	
	
	
		/*
	
	
	@Override
	public Collection<Result> check(Component component, ResultFactory resultFactory) {
		String stringParameterValue = stringParameter.getValue();
		Result result = resultFactory
			.create(stringParameterValue, "Description");
		return Collections.singleton(result);
	
	
	

	}*/
	
}


/* pseudeocode:
 * 
 * SMC runs through pre-check: GUID, (touching bounding boxes on two points on same x,y-level & height of all walls into list)
 * 
 * SMC runs check: if room == kitchen -> checkMinimumWidth of room, if big enough (pass : otherwise fail)
 * 	if room == habitable room != kitchen -> get Dimensions x,y,z (z from wall that touches bounding box on to different points on same x,y-plane)
 * 	
 * show rooms with their space dimensions
 * 
 * DONE
 */
