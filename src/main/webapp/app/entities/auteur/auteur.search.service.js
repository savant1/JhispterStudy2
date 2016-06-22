(function() {
    'use strict';

    angular
        .module('projetApp')
        .factory('AuteurSearch', AuteurSearch);

    AuteurSearch.$inject = ['$resource'];

    function AuteurSearch($resource) {
        var resourceUrl =  'api/_search/auteurs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
