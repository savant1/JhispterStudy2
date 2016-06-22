(function() {
    'use strict';

    angular
        .module('projetApp')
        .factory('NoterSearch', NoterSearch);

    NoterSearch.$inject = ['$resource'];

    function NoterSearch($resource) {
        var resourceUrl =  'api/_search/noters/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
