import java.awt.Image;
public class Goods extends THINGS{
	String name;
	Image ph;
	boolean ex=false;
	public Goods(){
	}
	public Goods(String name,int ID){
		this.name =name;
		this.ID=ID;	
	}
	void gotme() {
		this.ex=true;
	}
}

class jewel extends THINGS{
	String name;
	public jewel(String name,int ID){
		this.name=name;
		this.ID=ID;	
	}
}

class key extends THINGS{
	String name;
	int num=0;
	Image ph;
	public key(String name,int ID){
		this.name=name;
		this.ID=ID;	
	}
}

class door extends THINGS{
	String name;
	Image ph;
	public door(String name,int ID){
		this.name=name;
		this.ID=ID;	
	}
}

class shop extends THINGS{
	String name;
	Image ph;
	public shop(String name,int ID){
		this.name=name;
		this.ID=ID;	
	}
}



class drug extends THINGS{
	String name;
	int hp;
	Image ph;
	drug(String name ,int hp,int ID){
		this.name =name;
		this.hp=hp;
		this.ID=ID;	
	}
}

class sword extends THINGS{
	String name;
	int atk;
	Image ph;
	sword(String name ,int atk,int ID){
		this.name =name;
		this.atk=atk;
		this.ID=ID;	
	}
}

class shield extends THINGS{
	String name;
	int def;
	Image ph;
	shield(String name ,int def,int ID){
		this.name =name;
		this.def=def;
		this.ID=ID;	
	}
}

class stair extends THINGS{
	String name;
	Image ph;
	stair(String name,int ID){
		this.name =name;
		this.ID=ID;	
	}
}

