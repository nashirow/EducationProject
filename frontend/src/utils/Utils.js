 /**
  * Gestion d'une réponse HTTP négative
  * @param {Object} response 
  */
export const handleResponse = async (callback, response, json, callbackConfirmation) => {
    if(!response.ok){
        console.log(response);
        let listErrors = json.erreurs;
        if(response.status === 500){
            listErrors = [json.erreurs];
        }
        callback(response.status > 403 && response.status < 500 ? [process.env.REACT_APP_GENERAL_ERROR] : listErrors);
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
        start: 'Heure de début',
        end: 'Heure de fin',
        couleurFond: 'Couleur de fond (hexadécimal)',
        couleurPolice: 'Couleur de police (hexadécimal)',
        matiere: 'Matière',
        timeSlot: 'Créneaux horaires',
        salle: 'Salles',
        comment: 'Commentaires',
        jour: 'Jour',
        plannings: 'Emplois du temps liés',
        enseignant: 'Enseignant',
    };
    return mapping[key];
};