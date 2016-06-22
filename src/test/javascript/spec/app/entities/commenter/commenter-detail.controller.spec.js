'use strict';

describe('Controller Tests', function() {

    describe('Commenter Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockCommenter, MockOuvrage, MockUtilisateur;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockCommenter = jasmine.createSpy('MockCommenter');
            MockOuvrage = jasmine.createSpy('MockOuvrage');
            MockUtilisateur = jasmine.createSpy('MockUtilisateur');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Commenter': MockCommenter,
                'Ouvrage': MockOuvrage,
                'Utilisateur': MockUtilisateur
            };
            createController = function() {
                $injector.get('$controller')("CommenterDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'projetApp:commenterUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
