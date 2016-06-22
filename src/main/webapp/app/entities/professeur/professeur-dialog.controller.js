(function() {
    'use strict';

    angular
        .module('projetApp')
        .controller('ProfesseurDialogController', ProfesseurDialogController);

    ProfesseurDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Professeur', 'Utilisateur'];

    function ProfesseurDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Professeur, Utilisateur) {
        var vm = this;

        vm.professeur = entity;
        vm.clear = clear;
        vm.save = save;
        vm.utilisateurs = Utilisateur.query({filter: 'professeur-is-null'});
        $q.all([vm.professeur.$promise, vm.utilisateurs.$promise]).then(function() {
            if (!vm.professeur.utilisateur || !vm.professeur.utilisateur.id) {
                return $q.reject();
            }
            return Utilisateur.get({id : vm.professeur.utilisateur.id}).$promise;
        }).then(function(utilisateur) {
            vm.utilisateurs.push(utilisateur);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.professeur.id !== null) {
                Professeur.update(vm.professeur, onSaveSuccess, onSaveError);
            } else {
                Professeur.save(vm.professeur, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('projetApp:professeurUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
