import onetoone.*

class BootStrap {

    def init = { servletContext ->
        new Brother(name:"John").save();
        new Brother(name:"Tom").save()
        
        new Sister(name:"Alice").save()
        new Sister(name:"Mary").save(flush:true)
    }
    
    def destroy = {
    }
}
