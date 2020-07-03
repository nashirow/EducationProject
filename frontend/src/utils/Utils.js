 /**
  * Gestion d'une réponse HTTP négative
  * @param {Object} response 
  */
   export const handleResponse = async (callback, response, json) => {
    if(!response.ok){
        console.log(response);
        callback(response.status === 404 ? [process.env.REACT_APP_GENERAL_ERROR] : json.erreurs);
    }
    return response;
    };