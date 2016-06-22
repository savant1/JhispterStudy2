(function() {
    'use strict';

    angular
        .module('projetApp')
        .factory('ProfesseurSearch', ProfesseurSearch);

    ProfesseurSearch.$inject = ['$resource'];

    function ProfesseurSearch($resource) {
        var resourceUrl =  'api/_search/professeurs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
