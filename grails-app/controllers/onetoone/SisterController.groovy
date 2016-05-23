package onetoone

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class SisterController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Sister.list(params), model:[sisterCount: Sister.count()]
    }

   @Transactional
    def setFavouriteBrother(){
        Brother bro = Brother.get(params.id)
        log.info("Brother before:" + bro)
        
        def sister = new Sister();
        sister.name = "Jane"
        sister.favouriteBrother = bro;
        
        log.info("Sister: " + sister)
        log.info("Brother after:" + bro)
        
        sister.save(flush:true, failOnError:true)
        forward action:"index"
    }

    def show(Sister sister) {
        respond sister
    }

    def create() {
        respond new Sister(params)
    }

    @Transactional
    def save(Sister sister) {
        if (sister == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (sister.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond sister.errors, view:'create'
            return
        }

        sister.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'sister.label', default: 'Sister'), sister.id])
                redirect sister
            }
            '*' { respond sister, [status: CREATED] }
        }
    }

    def edit(Sister sister) {
        respond sister
    }

    @Transactional
    def update(Sister sister) {
        if (sister == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (sister.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond sister.errors, view:'edit'
            return
        }

        sister.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'sister.label', default: 'Sister'), sister.id])
                redirect sister
            }
            '*'{ respond sister, [status: OK] }
        }
    }

    @Transactional
    def delete(Sister sister) {

        if (sister == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        sister.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'sister.label', default: 'Sister'), sister.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'sister.label', default: 'Sister'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
