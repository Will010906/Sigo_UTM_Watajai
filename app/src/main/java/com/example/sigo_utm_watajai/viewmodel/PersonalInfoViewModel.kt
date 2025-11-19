import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * 1. Clase de datos (Data Class) que define la estructura de la información personal del usuario.
 * Se utiliza para contener y transferir los datos del formulario de edición.
 */
data class PersonalInfo(
    val nombre: String = "",
    val apellidoPaterno: String = "",
    val apellidoMaterno: String = "",
    val fechaNacimiento: String = "",
    val estadoNacimiento: Int = 0, // Índice del Spinner (se almacena la posición seleccionada)
    val sexo: Int = 0,           // Índice del Spinner (se almacena la posición seleccionada)
    val curp: String = "",
    val nss: String = ""
)

/**
 * ViewModel que gestiona y expone el estado de la información personal del usuario
 * a la capa de la interfaz de usuario.
 * Utiliza LiveData para la comunicación segura y reactiva.
 */
class PersonalInfoViewModel : ViewModel() {

    /**
     * 2. MutableLiveData: El contenedor mutable donde se almacenan los datos.
     * Es privado para que solo el ViewModel pueda modificar su valor (principio de encapsulación).
     * Se inicializa con un objeto PersonalInfo vacío.
     */
    private val _personalInfo = MutableLiveData<PersonalInfo>()

    /**
     * 3. LiveData: Es la versión inmutable que la Activity observará.
     * Esto asegura que la Activity solo pueda LEER el estado de los datos, no modificarlos directamente.
     */
    val personalInfo: LiveData<PersonalInfo> = _personalInfo

    /**
     * 4. Bloque de inicialización que se ejecuta al crear la instancia del ViewModel.
     */
    init {
        // Simulación de carga de datos iniciales o guardados desde el Repositorio/DB.
        _personalInfo.value = PersonalInfo(
            nombre = "Carlos",
            apellidoPaterno = "Maldonado",
            curp = "MDCG900101HDFLNR01"
            // El resto se inicializa en vacío/cero por defecto (como se define en la data class)
        )
    }

    /**
     * 5. Función utilizada para actualizar completamente el objeto LiveData.
     * La Activity llama a esta función con el nuevo estado del formulario.
     * @param newInfo El nuevo objeto PersonalInfo que contiene los datos del formulario.
     */
    fun updatePersonalInfo(newInfo: PersonalInfo) {
        _personalInfo.value = newInfo
    }

    /**
     * 6. Función para manejar la lógica de negocio asociada al botón "Actualizar" o "Guardar".
     *
     * @param currentInfo El objeto PersonalInfo actual con los datos ingresados por el usuario.
     */
    fun saveUpdates(currentInfo: PersonalInfo) {
        // Aquí iría la lógica de negocio real (en una app real, esto llamaría a un Repositorio):
        // - Validaciones (ej. CURP de 18 caracteres, campos obligatorios).
        // - Llamadas a la base de datos o API (usando viewModelScope.launch).

        if (currentInfo.nombre.isBlank()) {
            // Manejo de error o Toast de validación
            println("Error: El nombre no puede estar vacío")
            return
        }

        // Si todas las validaciones pasan, se actualiza el LiveData y se guarda en la capa de datos.
        _personalInfo.value = currentInfo
        println("Datos guardados exitosamente: $currentInfo")
    }
}