(function() {
    'use strict';
    angular
        .module('projetApp')
        .factory('Emprunter', Emprunter);

    Emprunter.$inject = ['$resource'];

    function Emprunter ($resource) {
        var resourceUrl =  'api/emprunters/:id';

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
