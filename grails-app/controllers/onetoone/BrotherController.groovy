package onetoone

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class BrotherController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Brother.list(params), model:[brotherCount: Brother.count()]
    }
    
    @Transactional
    def setFavouriteSister(){
        Sister sister = Sister.get(params.id)
        log.info("Sister before:" + sister)
        def brother = new Brother(favouriteSister: sister);
        brother.name = "Harry";
        
        log.info("Brother: " + brother)
        log.info("Sister after:" + sister)
        
        brother.save(flush:true, failOnError:true)
        forward action:"index"
    }

    def show(Brother brother) {
        respond brother
    }

    def create() {
        respond new Brother(params)
    }

    @Transactional
    def save(Brother brother) {
        if (brother == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (brother.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond brother.errors, view:'create'
            return
        }

        brother.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'brother.label', default: 'Brother'), brother.id])
                redirect brother
            }
            '*' { respond brother, [status: CREATED] }
        }
    }

    def edit(Brother brother) {
        respond brother
    }

    @Transactional
    def update(Brother brother) {
        if (brother == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (brother.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond brother.errors, view:'edit'
            return
        }

        brother.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'brother.label', default: 'Brother'), brother.id])
                redirect brother
            }
            '*'{ respond brother, [status: OK] }
        }
    }

    @Transactional
    def delete(Brother brother) {

        if (brother == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        brother.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'brother.label', default: 'Brother'), brother.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'brother.label', default: 'Brother'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
