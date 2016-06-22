(function() {
    'use strict';
    angular
        .module('projetApp')
        .factory('Professeur', Professeur);

    Professeur.$inject = ['$resource'];

    function Professeur ($resource) {
        var resourceUrl =  'api/professeurs/:id';

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
