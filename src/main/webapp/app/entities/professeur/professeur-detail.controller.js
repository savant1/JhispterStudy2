(function() {
    'use strict';

    angular
        .module('projetApp')
        .controller('ProfesseurDetailController', ProfesseurDetailController);

    ProfesseurDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Professeur', 'Utilisateur'];

    function ProfesseurDetailController($scope, $rootScope, $stateParams, entity, Professeur, Utilisateur) {
        var vm = this;

        vm.professeur = entity;

        var unsubscribe = $rootScope.$on('projetApp:professeurUpdate', function(event, result) {
            vm.professeur = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
