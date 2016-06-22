(function() {
    'use strict';

    angular
        .module('projetApp')
        .factory('OuvrageSearch', OuvrageSearch);

    OuvrageSearch.$inject = ['$resource'];

    function OuvrageSearch($resource) {
        var resourceUrl =  'api/_search/ouvrages/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
