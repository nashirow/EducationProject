 /**
  * Gestion d'une réponse HTTP négative
  * @param {Object} response 
  */
export const handleResponse = async (callback, response, json, callbackConfirmation) => {
    if(!response.ok){
        console.log(response);
        callback(response.status > 403 ? [process.env.REACT_APP_GENERAL_ERROR] : json.erreurs);
    }else if(response.ok && callbackConfirmation){
        callbackConfirmation();
    }
    return response;
};