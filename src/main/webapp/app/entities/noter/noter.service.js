(function() {
    'use strict';
    angular
        .module('projetApp')
        .factory('Noter', Noter);

    Noter.$inject = ['$resource'];

    function Noter ($resource) {
        var resourceUrl =  'api/noters/:id';

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
