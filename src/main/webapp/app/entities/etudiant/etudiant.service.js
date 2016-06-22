(function() {
    'use strict';
    angular
        .module('projetApp')
        .factory('Etudiant', Etudiant);

    Etudiant.$inject = ['$resource'];

    function Etudiant ($resource) {
        var resourceUrl =  'api/etudiants/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
