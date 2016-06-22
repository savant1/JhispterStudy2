(function() {
    'use strict';
    angular
        .module('projetApp')
        .factory('Recommander', Recommander);

    Recommander.$inject = ['$resource'];

    function Recommander ($resource) {
        var resourceUrl =  'api/recommanders/:id';

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
