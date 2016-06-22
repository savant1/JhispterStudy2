(function() {
    'use strict';

    angular
        .module('projetApp')
        .factory('RecommanderSearch', RecommanderSearch);

    RecommanderSearch.$inject = ['$resource'];

    function RecommanderSearch($resource) {
        var resourceUrl =  'api/_search/recommanders/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
