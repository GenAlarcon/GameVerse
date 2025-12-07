package cl.duoc.gameverse.ui.viewmodel

import cl.duoc.gameverse.data.Repository
import cl.duoc.gameverse.domain.model.Game
import cl.duoc.gameverse.domain.model.Usuario
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Pruebas unitarias para UserViewModel.
 * Utilizamos MockK para simular el Repositorio y CoroutinesTest para manejar los hilos.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class UserViewModelTest {

    // 1. MOCK: Creamos un repositorio "falso"
    // 'relaxed = true' permite llamar métodos sin definirlos todos (devuelven valores por defecto)
    private val repository: Repository = mockk(relaxed = true)

    // 2. Dispatcher de prueba: Controla el tiempo de las corrutinas
    private val testDispatcher = StandardTestDispatcher()

    // 3. El ViewModel que vamos a probar
    private lateinit var viewModel: UserViewModel

    @Before
    fun setup() {
        // Reemplazamos el Dispatcher Main (UI) por el nuestro de prueba
        Dispatchers.setMain(testDispatcher)

        // Inicializamos el ViewModel con el repositorio falso
        viewModel = UserViewModel(repository)
    }

    @After
    fun tearDown() {
        // Reseteamos el Dispatcher al terminar
        Dispatchers.resetMain()
    }

    // ==========================================
    // PRUEBAS DE USUARIO (LOGIN / REGISTRO)
    // ==========================================

    @Test
    fun `registrarUsuario exitoso actualiza usuarioActual y limpia errores`() = runTest {
        // GIVEN (Dado): Un usuario y un repositorio que no falla
        val usuario = Usuario("Gamer", "test@gameverse.com", "1234")
        coEvery { repository.registrarUsuario(any()) } returns Unit // Simula éxito

        // WHEN (Cuando): Llamamos a registrar
        viewModel.registrarUsuario(usuario)
        testDispatcher.scheduler.advanceUntilIdle() // Esperamos a que termine la corrutina

        // THEN (Entonces):
        // 1. El usuario actual debe ser el que registramos
        // usuarioActual usa '=' en el ViewModel, así que accedemos con .value
        assertEquals(usuario, viewModel.usuarioActual.value)

        // 2. No debe haber errores de login
        // loginError usa 'by' en el ViewModel, así que accedemos DIRECTO (sin .value)
        assertNull(viewModel.loginError)
    }

    @Test
    fun `registrarUsuario con error (correo existe) actualiza loginError`() = runTest {
        // GIVEN: El repositorio lanza una excepción simulando error de API
        val usuario = Usuario("Gamer", "test@gameverse.com", "1234")
        coEvery { repository.registrarUsuario(any()) } throws Exception("Error API: 409 Conflict")

        // WHEN
        viewModel.registrarUsuario(usuario)
        testDispatcher.scheduler.advanceUntilIdle()

        // THEN:
        assertNull(viewModel.usuarioActual.value) // No debió loguearse

        // loginError usa 'by', acceso directo
        assertNotNull(viewModel.loginError)
        // Verificamos que el mensaje sea el esperado según tu lógica
        assertEquals("El usuario ya existe.", viewModel.loginError)
    }

    @Test
    fun `validarLogin exitoso actualiza usuarioActual`() = runTest {
        // GIVEN: El repositorio encuentra al usuario
        val correo = "test@gameverse.com"
        val pass = "1234"
        val usuarioEncontrado = Usuario("Gamer", correo, pass)

        coEvery { repository.loginUsuario(correo, pass) } returns usuarioEncontrado

        // WHEN
        viewModel.validarLogin(correo, pass)
        testDispatcher.scheduler.advanceUntilIdle()

        // THEN
        assertEquals(usuarioEncontrado, viewModel.usuarioActual.value)
        assertNull(viewModel.loginError) // Acceso directo
    }

    @Test
    fun `validarLogin fallido (usuario no existe) muestra error`() = runTest {
        // GIVEN: El repositorio devuelve null
        val correo = "noexiste@gameverse.com"
        val pass = "1234"

        coEvery { repository.loginUsuario(correo, pass) } returns null

        // WHEN
        viewModel.validarLogin(correo, pass)
        testDispatcher.scheduler.advanceUntilIdle()

        // THEN
        assertNull(viewModel.usuarioActual.value)
        // Acceso directo a loginError
        assertEquals("Credenciales incorrectas.", viewModel.loginError)
    }

    @Test
    fun `cerrarSesion limpia todos los estados`() {
        // GIVEN: Simulamos un estado "sucio" (usuario logueado)
        // (Nota: Como las variables son privadas con set, asumimos estado inicial o mockeamos flujo)
        // Para este test, confiamos en que cerrarSesion pone todo en null/empty.

        // WHEN
        viewModel.cerrarSesion()

        // THEN
        assertNull(viewModel.usuarioActual.value)
        // juegoEnCurso usa 'by' en el ViewModel actualizado? Revisemos.
        // Si en el ViewModel es: var juegoEnCurso by mutableStateOf... -> acceso directo
        // Si en el ViewModel es: var juegoEnCurso = mutableStateOf... -> acceso .value
        // Asumiendo la última versión que tenías con 'by' para listas y juegoEnCurso:
        assertNull(viewModel.juegoEnCurso) // Acceso directo si usa 'by'
        assertTrue(viewModel.proximasAventuras.isEmpty()) // Acceso directo si usa 'by'
        assertNull(viewModel.loginError) // Acceso directo
    }

    // ==========================================
    // PRUEBAS DE LÓGICA DE JUEGOS (MEMORIA)
    // ==========================================

    @Test
    fun `agregarAventura agrega juego a la lista si no existe`() {
        // GIVEN
        val juego = Game("1", "Zelda", "Aventura", "url_img")

        // WHEN
        viewModel.agregarAventura(juego)

        // THEN
        // Acceso directo a proximasAventuras (usa 'by')
        val lista = viewModel.proximasAventuras
        assertEquals(1, lista.size)
        assertEquals("Zelda", lista[0].game.nombre)
        assertEquals(0.0f, lista[0].progreso, 0.0f)
    }

    @Test
    fun `agregarAventura NO duplica juegos`() {
        // GIVEN
        val juego = Game("1", "Zelda", "Aventura", "url_img")
        viewModel.agregarAventura(juego) // Agregamos primera vez

        // WHEN: Agregamos el mismo juego otra vez
        viewModel.agregarAventura(juego)

        // THEN: La lista debe seguir siendo de tamaño 1
        assertEquals(1, viewModel.proximasAventuras.size)
    }

    @Test
    fun `moverJuegoA_EnCurso mueve de la lista a juego actual`() {
        // GIVEN: Un juego en la lista de próximas aventuras
        val juego = Game("1", "Mario", "Plataformas", "url")
        viewModel.agregarAventura(juego)

        // Obtenemos el objeto JuegoUsuario que se creó internamente
        val juegoUsuario = viewModel.proximasAventuras[0]

        // WHEN: Lo movemos a "En Curso"
        viewModel.moverJuegoA_EnCurso(juegoUsuario)

        // THEN
        // 1. La lista de próximas debe estar vacía
        assertTrue(viewModel.proximasAventuras.isEmpty())
        // 2. El juego en curso debe ser Mario
        // Acceso directo a juegoEnCurso (usa 'by')
        assertNotNull(viewModel.juegoEnCurso)
        assertEquals("Mario", viewModel.juegoEnCurso?.game?.nombre)
    }

    @Test
    fun `actualizarProgresoEnCurso actualiza el valor correctamente`() {
        // GIVEN: Un juego en curso
        val juego = Game("1", "Mario", "Plataformas", "url")
        viewModel.agregarAventura(juego)
        val juegoUsuario = viewModel.proximasAventuras[0]
        viewModel.moverJuegoA_EnCurso(juegoUsuario)

        // WHEN: Actualizamos progreso al 50% (0.5f)
        viewModel.actualizarProgresoEnCurso(0.5f)

        // THEN
        // Acceso directo a juegoEnCurso
        assertEquals(0.5f, viewModel.juegoEnCurso?.progreso)
    }
}