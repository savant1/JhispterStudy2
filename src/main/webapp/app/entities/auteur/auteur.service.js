(function() {
    'use strict';
    angular
        .module('projetApp')
        .factory('Auteur', Auteur);

    Auteur.$inject = ['$resource'];

    function Auteur ($resource) {
        var resourceUrl =  'api/auteurs/:id';

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
