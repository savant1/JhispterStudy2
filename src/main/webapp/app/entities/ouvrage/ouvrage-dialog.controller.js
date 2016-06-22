(function() {
    'use strict';

    angular
        .module('projetApp')
        .controller('OuvrageDialogController', OuvrageDialogController);

    OuvrageDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Ouvrage', 'Realiser', 'Emprunter', 'Commenter', 'Noter', 'Recommander'];

    function OuvrageDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Ouvrage, Realiser, Emprunter, Commenter, Noter, Recommander) {
        var vm = this;

        vm.ouvrage = entity;
        vm.clear = clear;
        vm.save = save;
        vm.realisers = Realiser.query();
        vm.emprunters = Emprunter.query();
        vm.commenters = Commenter.query();
        vm.noters = Noter.query();
        vm.recommanders = Recommander.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.ouvrage.id !== null) {
                Ouvrage.update(vm.ouvrage, onSaveSuccess, onSaveError);
            } else {
                Ouvrage.save(vm.ouvrage, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('projetApp:ouvrageUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
