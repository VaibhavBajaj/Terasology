/*
 * Copyright 2013 Moving Blocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.entitySystem;

/**
 * An entity prefab describes the recipe for creating an entity.
 * Like an entity it groups a collection of components.
 *
 * @author Immortius <immortius@gmail.com>
 */
public interface Prefab extends ComponentContainer {

    /**
     * @return The identifier for this prefab
     */
    public String getName();

    /**
     * Iterate only over OWN components, excluding inheritance.
     * Required for proper serializing
     *
     * @return
     */
    public Iterable<Component> iterateOwnedComponents();

    /**
     * Return parents prefabs
     *
     * @return
     */
    public Iterable<Prefab> getParents();

    public void addParent(Prefab parent);

    public void removeParent(Prefab parent);

    public boolean isPersisted();

    public void setPersisted(boolean persisted);

}
