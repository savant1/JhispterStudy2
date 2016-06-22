(function() {
    'use strict';
    angular
        .module('projetApp')
        .factory('Commenter', Commenter);

    Commenter.$inject = ['$resource'];

    function Commenter ($resource) {
        var resourceUrl =  'api/commenters/:id';

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
