(function() {
    'use strict';

    angular
        .module('projetApp')
        .controller('EmprunterDialogController', EmprunterDialogController);

    EmprunterDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Emprunter', 'Ouvrage', 'Utilisateur'];

    function EmprunterDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Emprunter, Ouvrage, Utilisateur) {
        var vm = this;

        vm.emprunter = entity;
        vm.clear = clear;
        vm.save = save;
        vm.ouvrages = Ouvrage.query({filter: 'emprunter-is-null'});
        $q.all([vm.emprunter.$promise, vm.ouvrages.$promise]).then(function() {
            if (!vm.emprunter.ouvrage || !vm.emprunter.ouvrage.id) {
                return $q.reject();
            }
            return Ouvrage.get({id : vm.emprunter.ouvrage.id}).$promise;
        }).then(function(ouvrage) {
            vm.ouvrages.push(ouvrage);
        });
        vm.utilisateurs = Utilisateur.query({filter: 'emprunter-is-null'});
        $q.all([vm.emprunter.$promise, vm.utilisateurs.$promise]).then(function() {
            if (!vm.emprunter.utilisateur || !vm.emprunter.utilisateur.id) {
                return $q.reject();
            }
            return Utilisateur.get({id : vm.emprunter.utilisateur.id}).$promise;
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
            if (vm.emprunter.id !== null) {
                Emprunter.update(vm.emprunter, onSaveSuccess, onSaveError);
            } else {
                Emprunter.save(vm.emprunter, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('projetApp:emprunterUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
