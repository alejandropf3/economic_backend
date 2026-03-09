export const validar = (formulario, reglas) => {
  for (const nombreCampo in reglas) {
    const elemento = formulario.elements[nombreCampo];
    const regla = reglas[nombreCampo];
    
    if (!elemento) continue;
    
    // Validación de campo requerido
    if (regla.required && elemento.value.trim() === "") {
      return {
        esValido: false,
        mensaje: regla.mensaje,
        campo: nombreCampo
      };
    }
    
    // Validación de longitud mínima
    if (regla.min && elemento.value.length < regla.min) {
      return {
        esValido: false,
        mensaje: `El campo debe tener como mínimo ${regla.min} caracteres`,
        campo: nombreCampo
      };
    }
  }
  
  return {
    esValido: true
  };
};