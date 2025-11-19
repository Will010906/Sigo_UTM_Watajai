import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

// 1. Define una clase de datos simple para el formulario
// Esto ayuda a manejar los datos de forma estructurada
data class PersonalInfo(
    val nombre: String = "",
    val apellidoPaterno: String = "",
    val apellidoMaterno: String = "",
    val fechaNacimiento: String = "",
    val estadoNacimiento: Int = 0, // Índice del Spinner
    val sexo: Int = 0,           // Índice del Spinner
    val curp: String = "",
    val nss: String = ""
)

class PersonalInfoViewModel : ViewModel() {

    // 2. MutableLiveData: Donde se almacenan los datos que pueden cambiar.
    // Inicializamos con un objeto PersonalInfo vacío.
    private val _personalInfo = MutableLiveData<PersonalInfo>()

    // 3. LiveData: Es la versión inmutable que la Activity observará.
    // Esto asegura que la Activity solo pueda LEER, no modificar directamente.
    val personalInfo: LiveData<PersonalInfo> = _personalInfo

    // 4. Inicializa el LiveData
    init {
        // Simulación de carga de datos iniciales o guardados
        _personalInfo.value = PersonalInfo(
            nombre = "Carlos",
            apellidoPaterno = "Maldonado",
            curp = "MDCG900101HDFLNR01"
            // El resto se inicializa en vacío/cero por defecto
        )
    }

    // 5. Función para actualizar los datos desde la Activity/Fragment
    fun updatePersonalInfo(newInfo: PersonalInfo) {
        _personalInfo.value = newInfo
    }

    // 6. Función para manejar la lógica del botón "Actualizar"
    fun saveUpdates(currentInfo: PersonalInfo) {
        // Aquí iría la lógica de negocio real:
        // - Validaciones (ej. CURP de 18 caracteres)
        // - Llamadas a la base de datos o API (con Coroutines o Repositories)

        if (currentInfo.nombre.isBlank()) {
            // Manejo de error o Toast
            println("Error: El nombre no puede estar vacío")
            return
        }

        // Si todo es válido, actualiza el LiveData y guarda
        _personalInfo.value = currentInfo
        println("Datos guardados exitosamente: $currentInfo")
    }
}