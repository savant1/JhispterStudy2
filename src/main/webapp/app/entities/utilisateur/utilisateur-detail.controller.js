(function() {
    'use strict';

    angular
        .module('projetApp')
        .controller('UtilisateurDetailController', UtilisateurDetailController);

    UtilisateurDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'DataUtils', 'entity', 'Utilisateur', 'Emprunter', 'Commenter', 'Noter', 'Recommander'];

    function UtilisateurDetailController($scope, $rootScope, $stateParams, DataUtils, entity, Utilisateur, Emprunter, Commenter, Noter, Recommander) {
        var vm = this;

        vm.utilisateur = entity;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('projetApp:utilisateurUpdate', function(event, result) {
            vm.utilisateur = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
