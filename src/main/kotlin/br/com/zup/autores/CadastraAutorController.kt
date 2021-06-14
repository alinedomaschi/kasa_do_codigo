package br.com.zup.autores

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.http.uri.UriBuilder
import io.micronaut.validation.Validated
import javax.validation.Valid


@Validated
@Controller("/autores")
class CadastraAutorController(val autorRepository: AutorRepository,
                              val enderecoClient: EnderecoClient  ) {

    @Post
    //@Transactional
    fun cadastra(@Body @Valid request: NovoAutorRequest) : HttpResponse<Any>{
        println("Requesicao => ${request}")

        val enderecoResponse = enderecoClient.consulta(request.cep)

        val autor = request.paraAutor(enderecoResponse.body()!!)

        println("Autor ${autor.nome}")
        autorRepository.save(autor)

        val uri = UriBuilder.of("/autores/{id}")
                            .expand(mutableMapOf(Pair("id",autor.id)))

        return HttpResponse.created(uri)
    }

}