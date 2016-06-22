(function() {
    'use strict';
    angular
        .module('projetApp')
        .factory('Ouvrage', Ouvrage);

    Ouvrage.$inject = ['$resource'];

    function Ouvrage ($resource) {
        var resourceUrl =  'api/ouvrages/:id';

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
