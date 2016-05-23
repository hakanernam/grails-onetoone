package onetoone

class Brother {
    
    String name;
    
    Sister favouriteSister

    static constraints = {
            favouriteSister nullable:true
    }
    
    String toString(){
        "$id:$name --> ${favouriteSister?.name}"
    }
}
