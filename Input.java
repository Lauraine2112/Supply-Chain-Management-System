/**
 *  @author	Lauraine Baffot
 *  @author Rachel Renegado
 *  @author Chloe Bouchard
 *  @version 1.2
 *  @since 1.0
 */
 
package edu.ucalgary.ensf409;

/**
 * Input is a class that is responsible for moderating the user input to avoid 
 * exceptions and invalid inputs from being executed to the program. It validates
 * that the furniture category and type exist and correspond to each other.
 * It also checks if an input is a numeric value and if it is larger than 0
 */
public class Input {
	/**
	 * isFurnitureValid is a method that checks validity of furniture Category
	 * furniture category must be one of: desk, lamp, filing or chair if String
	 * input is part of the list, method returns true. If not, return false
	 * 
	 * @param input
	 * @return boolean
	 */
	public static boolean isFurnitureValid(String input) {
		//furniture can only be one of: desk, lamp, filing or chair
		if (input.equals("desk") || input.equals("lamp") || input.equals("filing") || input.equals("chair")) {
			return true;
		}
		return false;
	}

	/**
	 * isTypeValid is a method that checks validity of furniture Type, given the
	 * furniture Category. furniture category chair can only have furniture types:
	 * mesh , kneeling, task, executive and ergonomic furniture category desk can
	 * only have types: standing, adjustable and traditional furniture category lamp
	 * can only have
	 * 
	 * @param input
	 * @param furnitureCat
	 * @return boolean
	 */
	public static boolean isTypeValid(String furnitureCat, String input) {
		if (furnitureCat.equals("chair")) { //if furniture is chair, types can only be:
			if (input.equals("kneeling") || input.equals("task") || input.equals("mesh") || input.equals("executive")
					|| input.equals("ergonomic")) {
				return true;
			} else {
				return false;
			}
		} else if (furnitureCat.equals("desk")) { //if furniture is desk, types can only be:
			if (input.equals("standing") || input.equals("adjustable") || input.equals("traditional")) {
				return true;
			} else {
				return false;
			}
		} else if (furnitureCat.equals("lamp")) { //if furniture is lamp, types can only be:
			if (input.equals("desk") || input.equals("study") || input.equals("swing arm")) {
				return true;
			} else {
				return false;
			}
		} else if (furnitureCat.equals("filing")) { //if furniture is filing, types can only be:
			if (input.equals("small") || input.equals("medium") || input.equals("large")) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * isNumeric is a method that checks validity of a String input 's'. If this
	 * string is actually numeric, the method returns true. If the input is not
	 * numeric, the methor returns false
	 * 
	 * @param s
	 * @return boolean
	 */
	public static boolean isNumeric(String s) {
		return s != null && s.matches("[-+]?\\d*\\.?\\d+"); //ensures that a string is numeric
	}
}