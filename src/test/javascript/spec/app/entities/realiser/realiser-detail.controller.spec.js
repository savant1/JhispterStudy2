'use strict';

describe('Controller Tests', function() {

    describe('Realiser Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockRealiser, MockOuvrage, MockAuteur;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockRealiser = jasmine.createSpy('MockRealiser');
            MockOuvrage = jasmine.createSpy('MockOuvrage');
            MockAuteur = jasmine.createSpy('MockAuteur');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Realiser': MockRealiser,
                'Ouvrage': MockOuvrage,
                'Auteur': MockAuteur
            };
            createController = function() {
                $injector.get('$controller')("RealiserDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'projetApp:realiserUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
