(function() {
    'use strict';

    angular
        .module('projetApp')
        .factory('EtudiantSearch', EtudiantSearch);

    EtudiantSearch.$inject = ['$resource'];

    function EtudiantSearch($resource) {
        var resourceUrl =  'api/_search/etudiants/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
