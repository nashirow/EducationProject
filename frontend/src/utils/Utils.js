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

/**
 * Vérifie que la clé passée en paramètre est une clé de type date.
 * @param {String} key
 * @return boolean
 */
export const isKeyDate = (key) => key === 'creationDate' || key === 'modificationDate';

/**
 * Transforme la clé d'un objet résultat en un label plus compréhensible.
 * @param {String} key
 */
export const renderKey = (key) => {
    const mapping = {
        id: 'Identifiant',
        nom: 'Nom',
        prenom: 'Prénom',
        creationDate: 'Date de création',
        modificationDate: 'Dernière date de modification',
        volumeHoraire: 'Volume horaire',
        description: 'Description',
    };
    return mapping[key];
};