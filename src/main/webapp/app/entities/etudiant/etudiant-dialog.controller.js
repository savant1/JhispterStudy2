(function() {
    'use strict';

    angular
        .module('projetApp')
        .controller('EtudiantDialogController', EtudiantDialogController);

    EtudiantDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Etudiant', 'Utilisateur'];

    function EtudiantDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Etudiant, Utilisateur) {
        var vm = this;

        vm.etudiant = entity;
        vm.clear = clear;
        vm.save = save;
        vm.utilisateurs = Utilisateur.query({filter: 'etudiant-is-null'});
        $q.all([vm.etudiant.$promise, vm.utilisateurs.$promise]).then(function() {
            if (!vm.etudiant.utilisateur || !vm.etudiant.utilisateur.id) {
                return $q.reject();
            }
            return Utilisateur.get({id : vm.etudiant.utilisateur.id}).$promise;
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
            if (vm.etudiant.id !== null) {
                Etudiant.update(vm.etudiant, onSaveSuccess, onSaveError);
            } else {
                Etudiant.save(vm.etudiant, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('projetApp:etudiantUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
