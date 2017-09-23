package nqueens;

public class Tuple {
	public int b;
	public int a;
	
	Tuple(int a, int b){
		this.a = a;
		this.b = b;
	}
	
	public boolean equals(Tuple T) {
		if(T == null) return false;
		if(T.getClass() != getClass()) return false;
		Tuple other = (Tuple)T;
		if(this.a != other.a) return false;
		if(this.b != other.b) return false;
		return true;
	}
	
}
