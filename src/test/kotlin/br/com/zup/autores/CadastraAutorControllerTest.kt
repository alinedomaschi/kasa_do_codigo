package br.com.zup.autores

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import javax.inject.Inject

@MicronautTest
internal class CadastraAutorControllerTest{

    @field:Inject
    lateinit var enderecoClient: EnderecoClient

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Test
    fun `deve cadastrar um novo autor`(){

        val novoAutorRequest = NovoAutorRequest("Rafael Ponte","rafael.ponte@gmail.com","Principe dos oceanos","11222-333", "37")

        val enderecoResponse = EnderecoResponse( "Rua das Laranjeiras","Rio de Janeiro", "Rj")

        Mockito.`when`(enderecoClient.consulta(novoAutorRequest.cep)).thenReturn(HttpResponse.ok(enderecoResponse))

        val request = HttpRequest.POST ("/autores", novoAutorRequest)

        val response = client.toBlocking().exchange(request, Any::class.java)

        Assertions.assertEquals(HttpStatus.CREATED, response.status)
        Assertions.assertTrue(response.headers.contains("Location"))
        Assertions.assertTrue(response.header("Location")!!.matches("/autores/\\d".toRegex()))

    }
    @MockBean(EnderecoClient::class)
    fun enderecoMock() :EnderecoClient {
        return Mockito.mock(EnderecoClient::class.java)
    }

}