package edu.ucalgary.ensf409;

public class Input {
	

//	public static void isQ(String input) {
//		if (input.equals("quit")) {
//			System.out.println("Program terminated");
//			System.exit(1);
//		}
//	}


	/**
	 * isFurnitureValid is a method that checks validity of furniture Category
	 * furniture category must be one of: desk, lamp, filing or chair
	 * if String input is part of the list, method returns true. If not, return false
	 * @param input
	 * @return boolean
	 */
	public static boolean isFurnitureValid(String input) {
		if (input.equals("desk") || input.equals("lamp") || input.equals("filing") || input.equals("chair")) {
			return true;
		}
		return false;
	}

	/**
	 * isTypeValid is a method that checks validity of furniture Type, given the
	 * furniture Category. 
	 * furniture category chair can only have furniture types: mesh , kneeling, task, executive and ergonomic 
	 * furniture category desk can only have types: standing, adjustable and traditonal
	 * furniture category lamp can only have
	 * @param input
	 * @param furnitureCat
	 * @return boolean
	 */
	public static boolean isTypeValid(String furnitureCat, String input) {
		if (furnitureCat.equals("chair")) {
			if (input.equals("kneeling") || input.equals("task") || input.equals("mesh") || input.equals("executive")
					|| input.equals("ergonomic")) {
				return true;
			} else {
				return false;
			}
		} else if (furnitureCat.equals("desk")) {
			if (input.equals("standing") || input.equals("adjustable") || input.equals("traditional")) {
				return true;
			} else {
				return false;
			}
		} else if (furnitureCat.equals("lamp")) {
			if (input.equals("desk") || input.equals("study") || input.equals("swing arm")) {
				return true;
			} else {
				return false;
			}
		} else if (furnitureCat.equals("filing")) {
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
	 * isNumeric is a method that checks validity of a String input 's'
	 * If this string is actually numeric, the method returns true
	 * If the input is not numeric, the methor returns false
	 * @param s
	 * @return boolean
	 */
	public static boolean isNumeric(String s) {
		return s != null && s.matches("[-+]?\\d*\\.?\\d+");
	}
}