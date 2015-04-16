package jumpingalien.model;

import java.util.HashSet;
import java.util.Set;
import be.kuleuven.cs.som.annotate.*;

/**
 * A class of schools with an association with the class of slimes.
 * 
 * @author 	Andreas Schryvers & Jonathan Oostvogels
 * 			2e Bachelor ingenieurswetenschappen
 * 			Subversion repository: https://code.google.com/p/ogp-jumping-alien/
 */
public class School {

	public School() {
		slimes = new HashSet<Slime>();
	}
	
	@Basic
	public Set<Slime> getSlimes() {
		return slimes;
	}
	
	public boolean hasAsSlime(Slime slime) {
		return getSlimes().contains(slimes);
	}
	
	public void addAsSlime(Slime slime) {
		getSlimes().add(slime);
	}
	
	public void removeAsSlime(Slime slime) {
		getSlimes().remove(slime);
	}
	
	public int getNbSlimes() {
		return getSlimes().size();
	}
	
	public boolean canHaveAsSlime(Slime slime) {
		return slime != null;
	}
	
	private Set<Slime> slimes;


}
