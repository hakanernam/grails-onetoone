package onetoone

class Sister {
    
    String name;
    
    Brother favouriteBrother

    static constraints = {
        favouriteBrother nullable:true   
    }
    
     String toString(){
        "$id:$name --> ${favouriteBrother?.name}"
    }
}
